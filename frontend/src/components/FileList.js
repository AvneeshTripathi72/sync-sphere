import React, { useState, useEffect } from 'react';
import axios from 'axios';

function FileList() {
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentFolder, setCurrentFolder] = useState(null);
  const [folderHistory, setFolderHistory] = useState([]);

  useEffect(() => {
    fetchFiles();

    // Listen for file upload events
    const handleFileUploaded = () => {
      fetchFiles();
    };

    window.addEventListener('fileUploaded', handleFileUploaded);

    // Cleanup
    return () => {
      window.removeEventListener('fileUploaded', handleFileUploaded);
    };
  }, [currentFolder]);

  const fetchFiles = async () => {
    setLoading(true);
    setError('');

    try {
      const url = '/api/files/list' + (currentFolder ? `?parentFolderId=${currentFolder}` : '');
      const response = await axios.get(url);
      setFiles(response.data);
    } catch (err) {
      setError('Failed to load files. Please try again.');
      console.error('Error fetching files:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (fileId) => {
    if (!window.confirm('Are you sure you want to delete this file?')) {
      return;
    }

    try {
      await axios.delete(`/api/files/delete/${fileId}`);
      // Remove the deleted file from the state
      setFiles(files.filter(file => file.id !== fileId));
    } catch (err) {
      setError('Failed to delete file. Please try again.');
      console.error('Error deleting file:', err);
    }
  };

  const handleDownload = async (fileId, fileName) => {
    try {
      const response = await axios.get(`/api/files/downloads/${fileId}`, {
        responseType: 'blob'
      });

      // Create a URL for the blob
      const url = window.URL.createObjectURL(new Blob([response.data]));

      // Create a temporary link and click it to download
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', fileName);
      document.body.appendChild(link);
      link.click();

      // Clean up
      link.parentNode.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (err) {
      setError('Failed to download file. Please try again.');
      console.error('Error downloading file:', err);
    }
  };

  const navigateToFolder = (folderId) => {
    setFolderHistory([...folderHistory, currentFolder]);
    setCurrentFolder(folderId);
  };

  const navigateBack = () => {
    const previousFolder = folderHistory.pop();
    setFolderHistory([...folderHistory]);
    setCurrentFolder(previousFolder);
  };

  if (loading) {
    return <div>Loading files...</div>;
  }

  return (
    <div className="card">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h2>Your Files</h2>
        {currentFolder && (
          <button 
            className="btn" 
            onClick={navigateBack}
            style={{ marginLeft: '10px' }}
          >
            Back
          </button>
        )}
      </div>

      {error && (
        <div className="alert alert-danger">
          {error}
        </div>
      )}

      {files.length === 0 ? (
        <p>No files found. Upload some files to get started.</p>
      ) : (
        <div>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid #ddd' }}>
                <th style={{ textAlign: 'left', padding: '10px' }}>Name</th>
                <th style={{ textAlign: 'left', padding: '10px' }}>Type</th>
                <th style={{ textAlign: 'left', padding: '10px' }}>Size</th>
                <th style={{ textAlign: 'left', padding: '10px' }}>Created At</th>
                <th style={{ textAlign: 'center', padding: '10px' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {files.map(file => (
                <tr key={file.id} style={{ borderBottom: '1px solid #ddd' }}>
                  <td style={{ padding: '10px' }}>
                    {file.type === 'folder' ? (
                      <span 
                        style={{ cursor: 'pointer', color: '#4285f4' }}
                        onClick={() => navigateToFolder(file.id)}
                      >
                        📁 {file.name}
                      </span>
                    ) : (
                      <span>📄 {file.name}</span>
                    )}
                  </td>
                  <td style={{ padding: '10px' }}>{file.type}</td>
                  <td style={{ padding: '10px' }}>
                    {file.size ? `${Math.round(file.size / 1024)} KB` : '-'}
                  </td>
                  <td style={{ padding: '10px' }}>
                    {new Date(file.createdAt).toLocaleString()}
                  </td>
                  <td style={{ padding: '10px', textAlign: 'center' }}>
                    {file.type !== 'folder' && (
                      <button 
                        className="btn"
                        onClick={() => handleDownload(file.id, file.name)}
                        style={{ marginRight: '5px' }}
                      >
                        Download
                      </button>
                    )}
                    <button 
                      className="btn btn-danger"
                      onClick={() => handleDelete(file.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default FileList;
