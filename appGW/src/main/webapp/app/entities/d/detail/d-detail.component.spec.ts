import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DDetailComponent } from './d-detail.component';

describe('D Management Detail Component', () => {
  let comp: DDetailComponent;
  let fixture: ComponentFixture<DDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ d: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load d on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.d).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
