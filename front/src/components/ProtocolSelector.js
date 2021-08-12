import React from 'react';
import {Select} from 'antd';
import staticData from '../data/staticData';

const {Option} = Select;
const {PROTOCOL} = staticData;

const ProtocolSelector = ({defaultValue, onChange}) => {
  return (
    <Select defaultValue={defaultValue} className="select-protocol" onChange={onChange}>
      <Option value={PROTOCOL.HTTPS}>https://</Option>
      <Option value={PROTOCOL.HTTP}>http://</Option>
    </Select>
  );
};

export default ProtocolSelector;
