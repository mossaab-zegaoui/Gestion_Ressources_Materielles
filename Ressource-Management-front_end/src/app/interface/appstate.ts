import {DataState} from './datastate';

export interface updatePasswordState {
  dataState: DataState;
  updateSuccess?: boolean;
  error?: string;
  message?: string;
}

export interface AppState<T> {
  state?: DataState;
  data?: T;
  error?: string;
}

export interface resgisterState {
  dataState: DataState;
  registerSuccess?: boolean;
  error?: string;
  message?: string;
}

export interface LoginState {
  dataState: DataState;
  loginSuccess?: boolean;
  error?: string;
  message?: string;
}

export interface ResetState {
  dataState: DataState;
  resetSuccess?: boolean;
  error?: string;
}
