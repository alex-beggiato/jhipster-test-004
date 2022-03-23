import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AService } from '../service/a.service';

import { AComponent } from './a.component';

describe('A Management Component', () => {
  let comp: AComponent;
  let fixture: ComponentFixture<AComponent>;
  let service: AService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AComponent],
    })
      .overrideTemplate(AComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AService);

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
    expect(comp.aS?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
