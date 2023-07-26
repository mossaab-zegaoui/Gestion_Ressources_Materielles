import { EventTypes } from "./event-type";

export interface ToastEvent {
  type: EventTypes;
  title: string;
  message: string;
}