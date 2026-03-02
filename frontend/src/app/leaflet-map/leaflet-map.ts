import { Component, Input, AfterViewInit, OnChanges, SimpleChanges } from '@angular/core';
import * as L from 'leaflet';
import { Payload } from '../app.api';

@Component({
  selector: 'app-leaflet-map',
  template: '<div id="map" class="w-full h-full"></div>'
})
export class LeafletMapComponent implements AfterViewInit, OnChanges {

  @Input() trucks: Payload[] = [];

  private map!: L.Map;
  private markers = new Map<number, L.Marker>();

  ngAfterViewInit() {
    this.initMap();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['trucks'] && this.map) {
      this.updateMarkers();
    }
  }

  private initMap() {
    const baseMapURL = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';

    this.map = L.map('map').setView([13.7563, 100.5018], 6);

    L.tileLayer(baseMapURL, {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);
  }

  private updateMarkers() {
    this.trucks.forEach(truck => {
      const id = truck.truckinfo.id;
      const lat = truck.truckinfo.lat;
      const lng = truck.truckinfo.lng;

      if (this.markers.has(id)) {
        // ถ้ามี marker แล้ว → ขยับตำแหน่ง
        this.markers.get(id)!.setLatLng([lat, lng]);
      } else {
        // ถ้ายังไม่มี → สร้างใหม่
        const marker = L.marker([lat, lng])
          .addTo(this.map)
          .bindPopup(`Truck ${id}`);

        this.markers.set(id, marker);
      }
    });
  }
}