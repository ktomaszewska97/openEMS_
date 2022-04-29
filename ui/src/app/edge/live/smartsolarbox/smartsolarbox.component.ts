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
  selector: SmartSolarBox.SELECTOR,
  templateUrl: './smartsolarbox.component.html',
})
export class SmartSolarBox {
  private static readonly SELECTOR = 'smartsolarbox';

  @Input() private componentId: string;

  private edge: Edge = null;
  public component: EdgeConfig.Component = null;
  private previousState = false;

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
          SmartSolarBox.SELECTOR + this.componentId,
          [
            new ChannelAddress(this.componentId, 'SolarAmps'),
            new ChannelAddress(this.componentId, 'SolarVolts'),
            new ChannelAddress(this.componentId, 'SolarPower'),
            new ChannelAddress(this.componentId, 'BatteryAmps'),
            new ChannelAddress(this.componentId, 'BatteryVolts'),
            new ChannelAddress(this.componentId, 'BatterySoc'),
            new ChannelAddress(this.componentId, 'LoadAmps'),
            new ChannelAddress(this.componentId, 'LoadVolts'),
            new ChannelAddress(this.componentId, 'LoadPower'),
            new ChannelAddress(this.componentId, 'GenTotal'),
            new ChannelAddress(this.componentId, 'ConsumpTotal'),
            new ChannelAddress(this.componentId, 'BoxTemperature'),
            new ChannelAddress(this.componentId, 'SwitchStatus'),
          ]
        );
      });
    });
  }

  ngOnDestroy() {
    if (this.edge != null) {
      this.edge.unsubscribeChannels(
        this.websocket,
        SmartSolarBox.SELECTOR + this.componentId
      );
    }
  }

  hitButton() {
    if (this.edge) {
      if (this.previousState == false) {
        this.edge
          .sendRequest(
            this.websocket,
            new SetChannelValueRequest({
              componentId: this.component.id,
              channelId: 'ButtonValues',
              value: {
                switchStatus: 'true',
              },
            })
          )
          .then((response) => {
            this.service.toast('Turned ON', 'success');
          })
          .catch((reason) => {
            this.service.toast('Error', 'danger');
          });
        this.previousState = true;
      } else {
        this.edge
          .sendRequest(
            this.websocket,
            new SetChannelValueRequest({
              componentId: this.component.id,
              channelId: 'ButtonValues',
              value: {
                switchStatus: 'false',
              },
            })
          )
          .then((response) => {
            this.service.toast('Turned OFF', 'warning');
          })
          .catch((reason) => {
            this.service.toast('Error', 'danger');
          });
        this.previousState = false;
      }
    }
  }

  hitButtonTrue() {
    if (this.edge) {
      console.log('Before sendRequest');
      this.edge
        .sendRequest(
          this.websocket,
          new SetChannelValueRequest({
            componentId: this.component.id,
            channelId: 'ButtonValues',
            value: {
              switchStatus: 'true',
            },
          })
        )
        .then((response) => {
          this.service.toast('Success', 'success');
        })
        .catch((reason) => {
          this.service.toast('Error', 'danger');
        });
      console.log();
    }
  }

  hitButtonFalse() {
    if (this.edge) {
      console.log('Before sendRequest');
      this.edge
        .sendRequest(
          this.websocket,
          new SetChannelValueRequest({
            componentId: this.component.id,
            channelId: 'ButtonValues',
            value: {
              switchStatus: 'false',
            },
          })
        )
        .then((response) => {
          this.service.toast('Success', 'success');
        })
        .catch((reason) => {
          this.service.toast('Error', 'danger');
        });
      console.log();
    }
  }
}
