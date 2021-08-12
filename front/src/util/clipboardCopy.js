const clipboardCopy = (text) => {
  if (!document.queryCommandSupported('copy')) {
    return false;
  }

  const textarea = document.createElement('textarea');
  textarea.value = text;
  document.body.appendChild(textarea);
  textarea.focus();
  textarea.select();

  document.execCommand('copy');
  document.body.removeChild(textarea);
  return true;
};

export default clipboardCopy;
