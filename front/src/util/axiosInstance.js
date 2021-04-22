import axios from 'axios';
import config from '../config'

const instance = axios.create({
  baseURL: config.serverHost.api,
});

instance.defaults.timeout = 2500;

export default instance;
