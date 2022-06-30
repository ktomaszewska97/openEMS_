import { ActivatedRoute } from '@angular/router';
import {
  ChannelAddress,
  Edge,
  EdgeConfig,
  Service,
  Websocket,
} from '../../../shared/shared';
import { Component, Input, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { SetChannelValueRequest } from 'src/app/shared/jsonrpc/request/setChannelValueRequest';

@Component({
  selector: SonnenBattery.SELECTOR,
  templateUrl: './sonnenbattery.component.html',
})
export class SonnenBattery {
  private static readonly SELECTOR = 'sonnenbattery';

  @Input() private componentId: string;

  private edge: Edge = null;
  public component: EdgeConfig.Component = null;
  private previousModeState = 1;
  previousChargeState = 1;
  chargeValue: string = '';

  constructor(
    private route: ActivatedRoute,
    private websocket: Websocket,
    public modalController: ModalController,
    public service: Service
  ) {}

  ngOnInit() {
    this.service.setCurrentComponent('', this.route).then((edge) => {
      this.edge = edge;
      this.service.getConfig().then((config) => {
        this.component = config.getComponent(this.componentId);
        console.log('Component ID' + this.componentId);
        edge.subscribeChannels(
          this.websocket,
          SonnenBattery.SELECTOR + this.componentId,
          [
            new ChannelAddress(this.componentId, 'ConsumptionW'),
            new ChannelAddress(this.componentId, 'ProductionW'),
            new ChannelAddress(this.componentId, 'GridfeedinW'),
            new ChannelAddress(this.componentId, 'PacTotalW'),
            new ChannelAddress(this.componentId, 'Rsoc'),
            new ChannelAddress(this.componentId, 'Usoc'),
            new ChannelAddress(this.componentId, 'Fac'),
            new ChannelAddress(this.componentId, 'Uac'),
            new ChannelAddress(this.componentId, 'Ubat'),
            new ChannelAddress(this.componentId, 'Timestamp'),
            new ChannelAddress(this.componentId, 'Issysteminstalled'),
            new ChannelAddress(this.componentId, 'ChannelValues'),
            new ChannelAddress(this.componentId, 'ModeStatus'),
            new ChannelAddress(this.componentId, 'ChargeStatus'),
            new ChannelAddress(this.componentId, 'ChargeValue'),
          ]
        );
      });
    });
  }

  ngOnDestroy() {
    if (this.edge != null) {
      this.edge.unsubscribeChannels(
        this.websocket,
        SonnenBattery.SELECTOR + this.componentId
      );
    }
  }

  hitModeButton() {
    if (this.edge) {
      if (this.previousModeState == 2) {
        this.edge
          .sendRequest(
            this.websocket,
            new SetChannelValueRequest({
              componentId: this.component.id,
              channelId: 'ChannelValues',
              value: {
                modeStatus: 1,
              },
            })
          )
          .then((response) => {
            this.service.toast('Mode status MANUAL', 'success');
          })
          .catch((reason) => {
            this.service.toast('Error', 'danger');
          });
        this.previousModeState = 1;
      } else {
        this.edge
          .sendRequest(
            this.websocket,
            new SetChannelValueRequest({
              componentId: this.component.id,
              channelId: 'ChannelValues',
              value: {
                modeStatus: 2,
              },
            })
          )
          .then((response) => {
            this.service.toast('Mode status AUTOMATIC', 'warning');
          })
          .catch((reason) => {
            this.service.toast('Error', 'danger');
          });
        this.previousModeState = 2;
      }
    }
  }

  hitChargeStatusButton() {
    if (this.edge) {
      if (this.previousChargeState == 2) {
        this.edge
          .sendRequest(
            this.websocket,
            new SetChannelValueRequest({
              componentId: this.component.id,
              channelId: 'ChannelValues',
              value: {
                chargeStatus: 1,
              },
            })
          )
          .then((response) => {
            this.service.toast('Charge.', 'success');
          })
          .catch((reason) => {
            this.service.toast('Error', 'danger');
          });
        this.previousChargeState = 1;
      } else {
        this.edge
          .sendRequest(
            this.websocket,
            new SetChannelValueRequest({
              componentId: this.component.id,
              channelId: 'ChannelValues',
              value: {
                chargeStatus: 2,
              },
            })
          )
          .then((response) => {
            this.service.toast('Discharge.', 'warning');
          })
          .catch((reason) => {
            this.service.toast('Error', 'danger');
          });
        this.previousChargeState = 2;
      }
    }
  }

  charge() {
    if (this.edge) {
      // Charge
      this.edge
        .sendRequest(
          this.websocket,
          new SetChannelValueRequest({
            componentId: this.component.id,
            channelId: 'ChannelValues',
            value: {
              chargeStatus: 1,
              chargeValue: this.chargeValue,
            },
          })
        )
        .then((response) => {
          this.service.toast('Charge.', 'success');
        })
        .catch((reason) => {
          this.service.toast('Error', 'danger');
        });
      this.previousChargeState = 1;
    }
  }

  discharge() {
    if (this.edge) {
      // Discharge
      this.edge
        .sendRequest(
          this.websocket,
          new SetChannelValueRequest({
            componentId: this.component.id,
            channelId: 'ChannelValues',
            value: {
              chargeStatus: 2,
              chargeValue: this.chargeValue,
            },
          })
        )
        .then((response) => {
          this.service.toast('Charge.', 'success');
        })
        .catch((reason) => {
          this.service.toast('Error', 'danger');
        });
      this.previousChargeState = 1;
    }
  }
}
