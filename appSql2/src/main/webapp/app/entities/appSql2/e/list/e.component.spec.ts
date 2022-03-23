import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EService } from '../service/e.service';

import { EComponent } from './e.component';

describe('E Management Component', () => {
  let comp: EComponent;
  let fixture: ComponentFixture<EComponent>;
  let service: EService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EComponent],
    })
      .overrideTemplate(EComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.eS?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
