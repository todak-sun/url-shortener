import {useState} from 'react';

const useLocalStorage = (key) => {
  if (!localStorage.getItem(key)) {
    localStorage.setItem(key, JSON.stringify([]));
  }

  const [item, setItem] = useState(JSON.parse(localStorage.getItem(key)));

  const updateItem = (updator) => {
    setItem((prev) => {
      const curr = updator(prev);
      localStorage.setItem(key, JSON.stringify(curr));
      return curr;
    });
  };
  return [item, updateItem];
};

export default useLocalStorage;
