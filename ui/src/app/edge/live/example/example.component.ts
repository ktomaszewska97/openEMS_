import { ActivatedRoute } from '@angular/router';
import { ChannelAddress, Edge, EdgeConfig, Service, Websocket } from '../../../shared/shared';
import { Component, Input, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';


@Component({
  selector: ExampleComponent.SELECTOR,
  templateUrl: './example.component.html'
})
export class ExampleComponent {


  private static readonly SELECTOR = "example";

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
          edge.subscribeChannels(this.websocket, ExampleComponent.SELECTOR + this.componentId, [
            new ChannelAddress(this.componentId, "ArrayVoltage"),
            new ChannelAddress(this.componentId, "LoadPower"),
          ]);
      });
  });

  }

  ngOnDestroy() {
    if (this.edge != null) {
        this.edge.unsubscribeChannels(this.websocket, ExampleComponent.SELECTOR + this.componentId);
    }
}

}
