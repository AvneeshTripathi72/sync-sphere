import React, { useState, useRef } from 'react';
import { uploadFile } from '../services/fileService';
import { extractErrorMessage } from '../services/apiClient';

const MAX_FILE_SIZE_MB = 20;

function FileUpload() {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [parentFolderId, setParentFolderId] = useState('');
  const fileInputRef = useRef(null);

  const handleFileChange = (e) => {
    setFile(e.target.files[0] || null);
    setMessage('');
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file) {
      setError('Please select a file to upload');
      return;
    }

    if (file.size > MAX_FILE_SIZE_MB * 1024 * 1024) {
      setError(`File size should not exceed ${MAX_FILE_SIZE_MB} MB`);
      return;
    }

    setLoading(true);
    setMessage('');
    setError('');

    try {
      const folderId = parentFolderId.trim() ? Number(parentFolderId.trim()) : null;
      await uploadFile(file, folderId);

      setMessage('File uploaded successfully!');
      setFile(null);
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
      window.dispatchEvent(new CustomEvent('fileUploaded'));
    } catch (err) {
      setError(extractErrorMessage(err, 'Failed to upload file. Please try again.'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>Upload File</h2>

      {message && <div className="alert alert-success">{message}</div>}
      {error && <div className="alert alert-danger">{error}</div>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="fileInput">Select File</label>
          <input
            type="file"
            id="fileInput"
            ref={fileInputRef}
            className="form-control"
            onChange={handleFileChange}
          />
        </div>

        <div className="form-group">
          <label htmlFor="parentFolderId">Parent Folder ID (optional)</label>
          <input
            type="text"
            id="parentFolderId"
            value={parentFolderId}
            onChange={(e) => setParentFolderId(e.target.value)}
            className="form-control"
            placeholder="Leave empty for root"
          />
        </div>

        <button type="submit" className="btn" disabled={loading || !file}>
          {loading ? 'Uploading...' : 'Upload'}
        </button>
      </form>
    </div>
  );
}

export default FileUpload;
