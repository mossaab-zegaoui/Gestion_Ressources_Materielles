import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { Toast } from 'bootstrap';
import { fromEvent, take } from 'rxjs';
import { EventTypes } from '../../interface/event-type';

@Component({
  selector: 'app-toast',
  templateUrl: 'toast.component.html',
  styleUrls: ['toast.component.scss'],
})
export class ToastComponent implements OnInit {
  @Output() disposeEvent = new EventEmitter();

  @ViewChild('toastElement', { static: true })
  toastEl!: ElementRef;

  @Input()
  type!: EventTypes;

  @Input()
  title!: string;

  @Input()
  message!: string;

  toast!: Toast;
  static currentToast: Toast | undefined ;

  ngOnInit() {
    this.show();
  }

  show() {
    if (ToastComponent.currentToast) {
      ToastComponent.currentToast.hide();
    }

    this.toast = new Toast(
      this.toastEl.nativeElement,
      this.type === EventTypes.Error
        ? {
          autohide: true,
          delay: 2500,
          animation: true
        }
        : {
          autohide: true,
          delay: 2500,
          animation: true
        }
    );

    fromEvent(this.toastEl.nativeElement, 'hidden.bs.toast')
      .pipe(take(1))
      .subscribe(() => this.hide());

    this.toast.show();
    ToastComponent.currentToast = this.toast;
  }

  hide() {
    this.toast.dispose();
    ToastComponent.currentToast = undefined;
    this.disposeEvent.emit();
  }
}
