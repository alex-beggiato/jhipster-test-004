import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IA, A } from '../a.model';

import { AService } from './a.service';

describe('A Service', () => {
  let service: AService;
  let httpMock: HttpTestingController;
  let elemDefault: IA;
  let expectedResult: IA | IA[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      uid: 'AAAAAAA',
      entAAAcod: 'AAAAAAA',
      entAAAdsc: 'AAAAAAA',
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

    it('should create a A', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new A()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a A', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          entAAAcod: 'BBBBBB',
          entAAAdsc: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a A', () => {
      const patchObject = Object.assign({}, new A());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of A', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          entAAAcod: 'BBBBBB',
          entAAAdsc: 'BBBBBB',
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

    it('should delete a A', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAToCollectionIfMissing', () => {
      it('should add a A to an empty array', () => {
        const a: IA = { id: 123 };
        expectedResult = service.addAToCollectionIfMissing([], a);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(a);
      });

      it('should not add a A to an array that contains it', () => {
        const a: IA = { id: 123 };
        const aCollection: IA[] = [
          {
            ...a,
          },
          { id: 456 },
        ];
        expectedResult = service.addAToCollectionIfMissing(aCollection, a);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a A to an array that doesn't contain it", () => {
        const a: IA = { id: 123 };
        const aCollection: IA[] = [{ id: 456 }];
        expectedResult = service.addAToCollectionIfMissing(aCollection, a);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(a);
      });

      it('should add only unique A to an array', () => {
        const aArray: IA[] = [{ id: 123 }, { id: 456 }, { id: 25919 }];
        const aCollection: IA[] = [{ id: 123 }];
        expectedResult = service.addAToCollectionIfMissing(aCollection, ...aArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const a: IA = { id: 123 };
        const a2: IA = { id: 456 };
        expectedResult = service.addAToCollectionIfMissing([], a, a2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(a);
        expect(expectedResult).toContain(a2);
      });

      it('should accept null and undefined values', () => {
        const a: IA = { id: 123 };
        expectedResult = service.addAToCollectionIfMissing([], null, a, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(a);
      });

      it('should return initial array if no A is added', () => {
        const aCollection: IA[] = [{ id: 123 }];
        expectedResult = service.addAToCollectionIfMissing(aCollection, undefined, null);
        expect(expectedResult).toEqual(aCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
