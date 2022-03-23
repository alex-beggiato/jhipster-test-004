import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BService } from '../service/b.service';
import { IB, B } from '../b.model';
import { IA } from 'app/entities/a/a.model';
import { AService } from 'app/entities/a/service/a.service';

import { BUpdateComponent } from './b-update.component';

describe('B Management Update Component', () => {
  let comp: BUpdateComponent;
  let fixture: ComponentFixture<BUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bService: BService;
  let aService: AService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bService = TestBed.inject(BService);
    aService = TestBed.inject(AService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call A query and add missing value', () => {
      const b: IB = { id: 456 };
      const entAAAuid: IA = { id: 97267 };
      b.entAAAuid = entAAAuid;

      const aCollection: IA[] = [{ id: 81269 }];
      jest.spyOn(aService, 'query').mockReturnValue(of(new HttpResponse({ body: aCollection })));
      const additionalAS = [entAAAuid];
      const expectedCollection: IA[] = [...additionalAS, ...aCollection];
      jest.spyOn(aService, 'addAToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ b });
      comp.ngOnInit();

      expect(aService.query).toHaveBeenCalled();
      expect(aService.addAToCollectionIfMissing).toHaveBeenCalledWith(aCollection, ...additionalAS);
      expect(comp.aSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const b: IB = { id: 456 };
      const entAAAuid: IA = { id: 29296 };
      b.entAAAuid = entAAAuid;

      activatedRoute.data = of({ b });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(b));
      expect(comp.aSSharedCollection).toContain(entAAAuid);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<B>>();
      const b = { id: 123 };
      jest.spyOn(bService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ b });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: b }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bService.update).toHaveBeenCalledWith(b);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<B>>();
      const b = new B();
      jest.spyOn(bService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ b });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: b }));
      saveSubject.complete();

      // THEN
      expect(bService.create).toHaveBeenCalledWith(b);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<B>>();
      const b = { id: 123 };
      jest.spyOn(bService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ b });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bService.update).toHaveBeenCalledWith(b);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAById', () => {
      it('Should return tracked A primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
