import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ADetailComponent } from './a-detail.component';

describe('A Management Detail Component', () => {
  let comp: ADetailComponent;
  let fixture: ComponentFixture<ADetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ADetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ a: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ADetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ADetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load a on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.a).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
