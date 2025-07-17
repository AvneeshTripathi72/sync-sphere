import React, { useState, useRef } from 'react';
import axios from 'axios';

function FileUpload() {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [parentFolderId, setParentFolderId] = useState('');

  const fileInputRef = useRef(null);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setMessage('');
    setError('');
  };

  const handleParentFolderChange = (e) => {
    setParentFolderId(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file) {
      setError('Please select a file to upload');
      return;
    }

    const MAX_SIZE_MB = 20;
    if (file.size > MAX_SIZE_MB * 1024 * 1024) {
      setError(`File size should not exceed ${MAX_SIZE_MB} MB`);
      return;
    }

    setLoading(true);
    setMessage('');
    setError('');

    const formData = new FormData();
    formData.append('file', file);

    if (parentFolderId) {
      formData.append('parentFolderId', parentFolderId);
    }

    try {
      const response = await axios.post(
          `${import.meta.env.VITE_API_URL}/api/files/upload`,
          formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          }
      );

      setMessage('File uploaded successfully!');
      setFile(null);

      // Reset the file input
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }

      // Trigger global custom event to refresh file list
      window.dispatchEvent(new CustomEvent('fileUploaded'));

    } catch (err) {
      if (err.response) {
        setError(err.response.data || 'Failed to upload file. Please try again.');
      } else if (err.request) {
        setError('No response from server. Please check your network.');
      } else {
        setError('Unexpected error occurred.');
      }
      console.error('Error uploading file:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
      <div className="card p-4 shadow-md rounded-xl bg-white max-w-xl mx-auto mt-8">
        <h2 className="text-2xl font-semibold mb-4">Upload File</h2>

        {message && <div className="alert alert-success text-green-600">{message}</div>}
        {error && <div className="alert alert-danger text-red-600">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="fileInput" className="block font-medium mb-1">
              Select File
            </label>
            <input
                type="file"
                id="fileInput"
                ref={fileInputRef}
                className="form-control border rounded p-2 w-full"
                onChange={handleFileChange}
            />
          </div>

          <div className="mb-4">
            <label htmlFor="parentFolderId" className="block font-medium mb-1">
              Parent Folder ID (optional)
            </label>
            <input
                type="text"
                id="parentFolderId"
                value={parentFolderId}
                onChange={handleParentFolderChange}
                className="form-control border rounded p-2 w-full"
                placeholder="Leave empty for root"
            />
          </div>

          <button
              type="submit"
              className={`btn bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
              disabled={loading || !file}
          >
            {loading ? 'Uploading...' : 'Upload'}
          </button>
        </form>
      </div>
  );
}

export default FileUpload;
