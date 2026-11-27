import React, { useState } from 'react';
import { useFiles } from '../hooks/useFiles';
import { formatFileSize, formatTimestamp } from '../utils/formatters';

function FileList() {
  const {
    files,
    loading,
    error,
    hasHistory,
    removeFile,
    download,
    addFolder,
    navigateToFolder,
    navigateBack,
  } = useFiles();

  const [newFolderName, setNewFolderName] = useState('');

  const handleDelete = async (fileId) => {
    if (!window.confirm('Are you sure you want to delete this item?')) {
      return;
    }
    await removeFile(fileId);
  };

  const handleCreateFolder = async (e) => {
    e.preventDefault();
    if (!newFolderName.trim()) {
      return;
    }
    const created = await addFolder(newFolderName.trim());
    if (created) {
      setNewFolderName('');
    }
  };

  if (loading) {
    return <div>Loading files...</div>;
  }

  return (
    <div className="card">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h2>Your Files</h2>
        <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
          <form onSubmit={handleCreateFolder} style={{ display: 'flex', gap: '8px' }}>
            <input
              type="text"
              className="form-control"
              placeholder="New folder name"
              value={newFolderName}
              onChange={(e) => setNewFolderName(e.target.value)}
              style={{ width: '180px' }}
            />
            <button type="submit" className="btn">
              New Folder
            </button>
          </form>
          {hasHistory && (
            <button className="btn" onClick={navigateBack}>
              Back
            </button>
          )}
        </div>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      {files.length === 0 ? (
        <p>No files found. Upload some files to get started.</p>
      ) : (
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
            {files.map((file) => (
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
                <td style={{ padding: '10px' }}>{formatFileSize(file.size)}</td>
                <td style={{ padding: '10px' }}>{formatTimestamp(file.createdAt)}</td>
                <td style={{ padding: '10px', textAlign: 'center' }}>
                  {file.type !== 'folder' && (
                    <button
                      className="btn"
                      onClick={() => download(file.id, file.name)}
                      style={{ marginRight: '5px' }}
                    >
                      Download
                    </button>
                  )}
                  <button className="btn btn-danger" onClick={() => handleDelete(file.id)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default FileList;
