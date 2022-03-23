import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EDetailComponent } from './e-detail.component';

describe('E Management Detail Component', () => {
  let comp: EDetailComponent;
  let fixture: ComponentFixture<EDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ e: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load e on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.e).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
