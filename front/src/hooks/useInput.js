import {useState} from 'react';

const useInput = (initValue) => {
  const [value, setValue] = useState(initValue);
  const onChangeValue = (e) => {
    const val = e.target.value;
    setValue(val);
  };
  return [value, onChangeValue, setValue];
};

export default useInput;
