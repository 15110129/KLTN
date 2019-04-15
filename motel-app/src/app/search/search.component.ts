import {Component, HostListener, OnInit, ViewEncapsulation} from '@angular/core';
import {SearchForm} from '../model/searchForm';
import {Options} from 'ng5-slider';
import {ActivatedRoute, Router} from '@angular/router';
import {GeocodingApiServiceService} from '../service/geocoding-api-service.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SearchComponent implements OnInit {
  searchForm: SearchForm = new SearchForm();
  searchForm1: SearchForm = new SearchForm();

  priceOptions: Options = {
    floor: 0,
    ceil: 50,
    step: 0.5,
    noSwitching: true,
    hideLimitLabels: true,
    hidePointerLabels: true
  };

  priceOptions1: Options = {
    floor: 0,
    ceil: 50,
    step: 0.5,
    noSwitching: true,
    hideLimitLabels: true,
    hidePointerLabels: true
  };

  acreageOptions: Options = {
    floor: 0,
    ceil: 1000,
    step: 5,
    noSwitching: true,
    hideLimitLabels: true,
    hidePointerLabels: true
  };

  acreageOptions1: Options = {
    floor: 0,
    ceil: 1000,
    step: 5,
    noSwitching: true,
    hideLimitLabels: true,
    hidePointerLabels: true
  };
  address: string;
  zoom = 13;
  lat = 10.7756587;
  lng = 106.7004238;

  public innerWidth: any;

  display = 'none';

  constructor(private router: Router,
              private geocodingAPIService: GeocodingApiServiceService) {
  }

  ngOnInit() {
    this.innerWidth = window.innerWidth;
    this.setValueSearchForm();
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerWidth = window.innerWidth;
  }

  ClickedOut(event) {
    if (event.target.className === 'modal') {
      this.display = 'none';
    }
  }

  openSearchMaps() {
    this.display = 'block';
  }

  closeSearchMaps() {
    this.display = 'none';
  }

  setValueSearchForm() {
    this.searchForm.priceStart = this.searchForm1.priceStart = 0;
    this.searchForm.priceEnd = this.searchForm1.priceEnd = 50;
    this.searchForm.acreageStart = this.searchForm1.acreageStart = 0;
    this.searchForm.acreageEnd = this.searchForm1.acreageEnd = 1000;
    this.searchForm1.radius = 2;
    this.searchForm1.xCoordinate = 10.7756587;
    this.searchForm1.yCoordinate = 106.7004238;
  }

  navigateToSearchPage() {
    this.router.navigate(['/ket-qua'], {queryParams: this.searchForm, skipLocationChange: false});
  }

  navigateToSearchPage1() {
    this.router.navigate(['/ket-qua'], {queryParams: this.searchForm1, skipLocationChange: false});
  }

  mapClicked($event: any) {
    this.searchForm1.xCoordinate = $event.coords.lat;
    this.searchForm1.yCoordinate = $event.coords.lng;
  }

  markerDragEnd($event: any) {
    this.searchForm1.xCoordinate = $event.coords.lat;
    this.searchForm1.yCoordinate = $event.coords.lng;
  }

  updateLatLngFromAddress() {
    this.geocodingAPIService
      .findFromAddress(this.address).subscribe(response => {
        if (response.status === 'OK') {
          this.lat = response.results[0].geometry.location.lat;
          this.lng = response.results[0].geometry.location.lng;
          this.searchForm1.xCoordinate = response.results[0].geometry.location.lat;
          this.searchForm1.yCoordinate = response.results[0].geometry.location.lng;
          console.log('GEO ' + this.lat);
          console.log('GEO ' + this.lng);
        } else if (response.status === 'ZERO_RESULTS') {
          console.log('geocodingAPIService', 'ZERO_RESULTS', response.status);
        } else {
          console.log('geocodingAPIService', 'Other error', response.status);
        }
      });
  }
}
