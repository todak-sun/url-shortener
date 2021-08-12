import React, {useState} from 'react';
import {List, Button, Modal, message} from 'antd';
import config from '../config';
import axios from '../util/axiosInstance';

const LogModal = ({log}) => {
  // TODO : 수정
  const {path, url, requestCount, logs} = log.data;
  return (
    <Modal visible={true} title={path + ' 로그 확인'} okText="닫기">
      <p>
        이동 URL : <a href={url}>{url}</a>
      </p>
      <p>요청 수 : {requestCount}</p>
      <List
        bordered
        dataSource={logs}
        renderItem={({ip, referer, time}) => (
          <List.Item>
            <span>ip : {ip}</span>
            <span>referer : {referer ? referer : '정보없음'}</span>
            <span>time : {time}</span>
          </List.Item>
        )}
      />
    </Modal>
  );
};

const MyUrlList = ({data, removeItem}) => {
  const [logData, setLogData] = useState(null);

  const onClickRemove = (key) => () => {
    removeItem(key);
  };

  const onClickLog = (path) => async () => {
    try {
      const {data} = await axios.get(`/shorts/${path}/logs`);
      setLogData(data);
    } catch (e) {
      const notFoundResource = e?.response?.data?.notFoundResource
      if(notFoundResource){
        message.error(`${notFoundResource}에 기록된 로그 정보가 없습니다.`)
      }
    }
  };

  return (
    <>
      {logData ? <LogModal log={logData} /> : ''}
      <List
        bordered
        dataSource={data}
        renderItem={(item) => (
          <List.Item key={item.key} actions={[<Button onClick={onClickLog(item.path)}>로그</Button>, <Button onClick={onClickRemove(item.key)}>삭제</Button>]}>
            <span>{config.serverHost.redirect.concat('/', item.path)}</span>
          </List.Item>
        )}
      />
    </>
  );
};

export default MyUrlList;
