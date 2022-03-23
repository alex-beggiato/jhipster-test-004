import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CDetailComponent } from './c-detail.component';

describe('C Management Detail Component', () => {
  let comp: CDetailComponent;
  let fixture: ComponentFixture<CDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ c: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load c on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.c).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
