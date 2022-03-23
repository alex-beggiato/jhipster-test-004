import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EService } from '../service/e.service';
import { IE, E } from '../e.model';
import { IC } from 'app/entities/c/c.model';
import { CService } from 'app/entities/c/service/c.service';

import { EUpdateComponent } from './e-update.component';

describe('E Management Update Component', () => {
  let comp: EUpdateComponent;
  let fixture: ComponentFixture<EUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eService: EService;
  let cService: CService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EUpdateComponent],
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
      .overrideTemplate(EUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eService = TestBed.inject(EService);
    cService = TestBed.inject(CService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call C query and add missing value', () => {
      const e: IE = { id: 456 };
      const entCCCuid: IC = { id: 59179 };
      e.entCCCuid = entCCCuid;

      const cCollection: IC[] = [{ id: 52309 }];
      jest.spyOn(cService, 'query').mockReturnValue(of(new HttpResponse({ body: cCollection })));
      const additionalCS = [entCCCuid];
      const expectedCollection: IC[] = [...additionalCS, ...cCollection];
      jest.spyOn(cService, 'addCToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ e });
      comp.ngOnInit();

      expect(cService.query).toHaveBeenCalled();
      expect(cService.addCToCollectionIfMissing).toHaveBeenCalledWith(cCollection, ...additionalCS);
      expect(comp.cSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const e: IE = { id: 456 };
      const entCCCuid: IC = { id: 65130 };
      e.entCCCuid = entCCCuid;

      activatedRoute.data = of({ e });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(e));
      expect(comp.cSSharedCollection).toContain(entCCCuid);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<E>>();
      const e = { id: 123 };
      jest.spyOn(eService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ e });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: e }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(eService.update).toHaveBeenCalledWith(e);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<E>>();
      const e = new E();
      jest.spyOn(eService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ e });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: e }));
      saveSubject.complete();

      // THEN
      expect(eService.create).toHaveBeenCalledWith(e);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<E>>();
      const e = { id: 123 };
      jest.spyOn(eService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ e });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eService.update).toHaveBeenCalledWith(e);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCById', () => {
      it('Should return tracked C primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
