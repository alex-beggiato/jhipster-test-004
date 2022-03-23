import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IE, E } from '../e.model';

import { EService } from './e.service';

describe('E Service', () => {
  let service: EService;
  let httpMock: HttpTestingController;
  let elemDefault: IE;
  let expectedResult: IE | IE[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      uid: 'AAAAAAA',
      entCCCuid: 0,
      entEEEcod: 'AAAAAAA',
      entEEEdsc: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a E', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new E()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a E', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          entCCCuid: 1,
          entEEEcod: 'BBBBBB',
          entEEEdsc: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a E', () => {
      const patchObject = Object.assign(
        {
          uid: 'BBBBBB',
          entCCCuid: 1,
        },
        new E()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of E', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          entCCCuid: 1,
          entEEEcod: 'BBBBBB',
          entEEEdsc: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a E', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEToCollectionIfMissing', () => {
      it('should add a E to an empty array', () => {
        const e: IE = { id: 123 };
        expectedResult = service.addEToCollectionIfMissing([], e);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(e);
      });

      it('should not add a E to an array that contains it', () => {
        const e: IE = { id: 123 };
        const eCollection: IE[] = [
          {
            ...e,
          },
          { id: 456 },
        ];
        expectedResult = service.addEToCollectionIfMissing(eCollection, e);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a E to an array that doesn't contain it", () => {
        const e: IE = { id: 123 };
        const eCollection: IE[] = [{ id: 456 }];
        expectedResult = service.addEToCollectionIfMissing(eCollection, e);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(e);
      });

      it('should add only unique E to an array', () => {
        const eArray: IE[] = [{ id: 123 }, { id: 456 }, { id: 16178 }];
        const eCollection: IE[] = [{ id: 123 }];
        expectedResult = service.addEToCollectionIfMissing(eCollection, ...eArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const e: IE = { id: 123 };
        const e2: IE = { id: 456 };
        expectedResult = service.addEToCollectionIfMissing([], e, e2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(e);
        expect(expectedResult).toContain(e2);
      });

      it('should accept null and undefined values', () => {
        const e: IE = { id: 123 };
        expectedResult = service.addEToCollectionIfMissing([], null, e, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(e);
      });

      it('should return initial array if no E is added', () => {
        const eCollection: IE[] = [{ id: 123 }];
        expectedResult = service.addEToCollectionIfMissing(eCollection, undefined, null);
        expect(expectedResult).toEqual(eCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
