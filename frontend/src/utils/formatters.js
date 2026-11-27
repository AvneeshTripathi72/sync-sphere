export function formatFileSize(bytes) {
  if (!bytes || bytes <= 0) {
    return '-';
  }
  if (bytes < 1024) {
    return `${bytes} B`;
  }
  const kb = Math.round(bytes / 1024);
  if (kb < 1024) {
    return `${kb} KB`;
  }
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

export function formatTimestamp(value) {
  if (!value) {
    return '-';
  }
  return new Date(value).toLocaleString();
}
