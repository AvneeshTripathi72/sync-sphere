import apiClient from './apiClient';

export async function listFiles(parentFolderId = null) {
  const params = parentFolderId ? { parentFolderId } : undefined;
  const { data } = await apiClient.get('/api/files/list', { params });
  return data;
}

export async function uploadFile(file, parentFolderId = null) {
  const formData = new FormData();
  formData.append('file', file);
  if (parentFolderId) {
    formData.append('parentFolderId', parentFolderId);
  }
  const { data } = await apiClient.post('/api/files/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return data;
}

export async function createFolder(name, parentFolderId = null) {
  const { data } = await apiClient.post('/api/files/folders', {
    name,
    parentFolderId,
  });
  return data;
}

export async function downloadFile(fileId, fileName) {
  const response = await apiClient.get(`/api/files/downloads/${fileId}`, {
    responseType: 'blob',
  });
  const blobUrl = window.URL.createObjectURL(new Blob([response.data]));
  const link = document.createElement('a');
  link.href = blobUrl;
  link.setAttribute('download', fileName);
  document.body.appendChild(link);
  link.click();
  link.parentNode.removeChild(link);
  window.URL.revokeObjectURL(blobUrl);
}

export async function deleteFile(fileId) {
  const { data } = await apiClient.delete(`/api/files/delete/${fileId}`);
  return data;
}
