import './App.css';
import {Input, Layout, Button, Tooltip, Row, Col, Divider, message, Popover} from 'antd';
import React, {useState} from 'react';
import useInput from './hooks/useInput';
import useLocalStorage from './hooks/useLocalStorage';
import staticData from './data/staticData';
import ProtocolSelector from './components/ProtocolSelector';
import MyUrlList from './components/MyUrlList';
import axios from './util/axiosInstance';
import clipboardCopy from './util/clipboardCopy';
import config from './config';
import shortid from 'shortid';

const {PROTOCOL} = staticData;
const {Content} = Layout;

const App = () => {
  const [isCreating, setIsCreating] = useState(false);
  const [resultUrl, setResultUrl] = useState('');
  const [hoverdResultUrl, setHoveredResult] = useState(false);
  const [clickedResultUrl, setClickedResultUrl] = useState(false);
  const [protocol, setProtocol] = useState(PROTOCOL.HTTPS);
  const [url, onChangeUrl, setUrl] = useInput('');
  const [myUrls, updateUrls] = useLocalStorage('MY_URL');

  const onClickBtnCreateUrl = async () => {
    if (!url.trim()) {
      return;
    }

    setIsCreating((status) => !status);

    const msg = message.loading('URL을 생성중입니다...', 0);

    Object.entries(PROTOCOL).forEach(([key, value]) => {
      if (url.startsWith(value)) {
        const changedUrl = url.replace(value, '');
        setUrl(changedUrl);
      }
    });

    try {
      const res = await axios.post('/', {url: protocol.concat(url)});
      const {path} = res.data;
      const createdUrl = config.serverHost.redirect.concat('/', path);
      updateUrls((item) => [...item, {key: shortid(), path: path}]);

      setResultUrl(createdUrl);
    } catch (e) {
      console.error(e);
    } finally {
      setTimeout(msg, 0);
      setIsCreating((status) => !status);
    }
  };

  const onChangeProtocol = (value) => {
    setProtocol(value);
  };

  const onHoverPopover = (visible) => {
    setClickedResultUrl(false);
    setHoveredResult(visible);
  };

  const onClickPopover = (visible) => {
    setClickedResultUrl(visible);
    setHoveredResult(false);
    const success = clipboardCopy(resultUrl);
  };

  return (
    <Layout style={{width: '75%', margin: '0 auto', marginTop: '300px'}}>
      <Content>
        <Divider orientation="center">내가 생성한 URL</Divider>
        {myUrls.length ? (
          <MyUrlList
            data={myUrls}
            removeItem={(key) => {
              updateUrls((items) => items.filter((item) => item.key !== key));
            }}
          />
        ) : (
          ''
        )}
        <Divider orientation="center">URL 입력</Divider>
        <Row gutter={10}>
          <Col span={20}>
            <Tooltip trigger={['focus']} title="URL을 입력해주세요." placement="top">
              <Input addonBefore={<ProtocolSelector defaultValue={PROTOCOL.HTTPS} onChange={onChangeProtocol} />} value={url} onInput={onChangeUrl} onPressEnter={onClickBtnCreateUrl} />
            </Tooltip>
          </Col>
          <Col span={4}>
            <Button type="primary" onClick={onClickBtnCreateUrl} disabled={isCreating}>
              생성
            </Button>
          </Col>
        </Row>
        {resultUrl ? (
          <>
            <Divider orientation="center">생성된 URL</Divider>
            <Row>
              <Popover content="클릭하면 Clipboard에 저장해요!" visible={hoverdResultUrl} onVisibleChange={onHoverPopover} trigger="hover">
                <Popover
                  content={
                    <div>
                      <p>Clipboard에 저장되었습니다!</p>
                      <Button
                        onClick={() => {
                          setClickedResultUrl(false);
                          setHoveredResult(false);
                        }}>
                        닫기
                      </Button>
                    </div>
                  }
                  visible={clickedResultUrl}
                  onVisibleChange={onClickPopover}
                  trigger="click">
                  <Input style={{width: '100%'}} value={resultUrl} readOnly={true}></Input>
                </Popover>
              </Popover>
            </Row>
          </>
        ) : (
          ''
        )}
      </Content>
    </Layout>
  );
};

export default App;
