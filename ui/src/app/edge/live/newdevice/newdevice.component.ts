import { ActivatedRoute } from '@angular/router';
import { ChannelAddress, Edge, EdgeConfig, Service, Websocket } from '../../../shared/shared';
import { Component, Input, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { SetChannelValueRequest } from 'src/app/shared/jsonrpc/request/setChannelValueRequest';


@Component({
  selector: NewDevice.SELECTOR,
  templateUrl: './newdevice.component.html'
})
export class NewDevice {

  private static readonly SELECTOR = "newdevice";

  @Input() private componentId: string;

  private edge: Edge = null;
  public component: EdgeConfig.Component = null;

  constructor(
    private route: ActivatedRoute,
    private websocket: Websocket,
    public modalController: ModalController,
    public service: Service,
  ) { }

  ngOnInit() {
    this.service.setCurrentComponent('', this.route).then(edge => {
      this.edge = edge;
      this.service.getConfig().then(config => {
          this.component = config.getComponent(this.componentId);
          console.log("Component ID"+this.componentId);
          edge.subscribeChannels(this.websocket, NewDevice.SELECTOR + this.componentId, [
            new ChannelAddress(this.componentId, "ArrayVoltage"),
            new ChannelAddress(this.componentId, "LoadPower"),
            new ChannelAddress(this.componentId, "SwitchState"),
            new ChannelAddress(this.componentId, "IsActive"),
          ]);
      });
  });
  }

  ngOnDestroy() {
    if (this.edge != null) {
        this.edge.unsubscribeChannels(this.websocket, NewDevice.SELECTOR + this.componentId);
    }
}

hitButton() {
  if (this.edge) {
    console.log("Before sendRequest");
      this.edge.sendRequest(
          this.websocket,
          new SetChannelValueRequest({
              componentId: this.component.id,
              channelId: 'ButtonValues',
              value: {
                "isTriggered": 'true',
                "isActive": 'true'
            }
          })
      ).then(response => {
          this.service.toast("Success", "success");
          
      }).catch(reason => {
          this.service.toast("Error", "danger");
      });
      console.log();
  }
}


}
