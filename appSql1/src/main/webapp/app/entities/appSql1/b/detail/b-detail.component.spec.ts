import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BDetailComponent } from './b-detail.component';

describe('B Management Detail Component', () => {
  let comp: BDetailComponent;
  let fixture: ComponentFixture<BDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ b: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load b on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.b).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
