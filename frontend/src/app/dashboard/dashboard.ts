import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { WebsocketService } from '../app.api';
import { LeafletMapComponent } from '../leaflet-map/leaflet-map';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [LeafletMapComponent],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit, OnDestroy {

  private ws = inject(WebsocketService);

  payloadList = this.ws.payloadList;
  payloadLength = this.ws.payloadLength;

  ngOnInit(): void {
    this.ws.connect();
  }

  ngOnDestroy(): void {
    this.ws.disconnect();
  }
}