import { useState, useEffect, useCallback } from 'react';
import { listFiles, deleteFile, downloadFile, createFolder } from '../services/fileService';
import { extractErrorMessage } from '../services/apiClient';

export function useFiles() {
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentFolder, setCurrentFolder] = useState(null);
  const [folderHistory, setFolderHistory] = useState([]);

  const fetchFiles = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const data = await listFiles(currentFolder);
      setFiles(data);
    } catch (err) {
      setError(extractErrorMessage(err, 'Failed to load files. Please try again.'));
    } finally {
      setLoading(false);
    }
  }, [currentFolder]);

  useEffect(() => {
    fetchFiles();

    const handleRefresh = () => fetchFiles();
    window.addEventListener('fileUploaded', handleRefresh);
    return () => window.removeEventListener('fileUploaded', handleRefresh);
  }, [fetchFiles]);

  const removeFile = async (fileId) => {
    try {
      await deleteFile(fileId);
      setFiles((prev) => prev.filter((file) => file.id !== fileId));
    } catch (err) {
      setError(extractErrorMessage(err, 'Failed to delete file. Please try again.'));
    }
  };

  const download = async (fileId, fileName) => {
    try {
      await downloadFile(fileId, fileName);
    } catch (err) {
      setError(extractErrorMessage(err, 'Failed to download file. Please try again.'));
    }
  };

  const addFolder = async (folderName) => {
    try {
      const created = await createFolder(folderName, currentFolder);
      setFiles((prev) => [...prev, created]);
      return true;
    } catch (err) {
      setError(extractErrorMessage(err, 'Failed to create folder.'));
      return false;
    }
  };

  const navigateToFolder = (folderId) => {
    setFolderHistory((prev) => [...prev, currentFolder]);
    setCurrentFolder(folderId);
  };

  const navigateBack = () => {
    setFolderHistory((prev) => {
      const updated = [...prev];
      const previous = updated.pop() ?? null;
      setCurrentFolder(previous);
      return updated;
    });
  };

  return {
    files,
    loading,
    error,
    currentFolder,
    hasHistory: folderHistory.length > 0 || currentFolder !== null,
    removeFile,
    download,
    addFolder,
    navigateToFolder,
    navigateBack,
  };
}
