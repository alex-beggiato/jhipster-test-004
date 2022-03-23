import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ID, D } from '../d.model';

import { DService } from './d.service';

describe('D Service', () => {
  let service: DService;
  let httpMock: HttpTestingController;
  let elemDefault: ID;
  let expectedResult: ID | ID[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      uid: 'AAAAAAA',
      entDDDcod: 'AAAAAAA',
      entDDDdsc: 'AAAAAAA',
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

    it('should create a D', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new D()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a D', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          entDDDcod: 'BBBBBB',
          entDDDdsc: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a D', () => {
      const patchObject = Object.assign(
        {
          uid: 'BBBBBB',
          entDDDdsc: 'BBBBBB',
        },
        new D()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of D', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          entDDDcod: 'BBBBBB',
          entDDDdsc: 'BBBBBB',
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

    it('should delete a D', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDToCollectionIfMissing', () => {
      it('should add a D to an empty array', () => {
        const d: ID = { id: 123 };
        expectedResult = service.addDToCollectionIfMissing([], d);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(d);
      });

      it('should not add a D to an array that contains it', () => {
        const d: ID = { id: 123 };
        const dCollection: ID[] = [
          {
            ...d,
          },
          { id: 456 },
        ];
        expectedResult = service.addDToCollectionIfMissing(dCollection, d);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a D to an array that doesn't contain it", () => {
        const d: ID = { id: 123 };
        const dCollection: ID[] = [{ id: 456 }];
        expectedResult = service.addDToCollectionIfMissing(dCollection, d);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(d);
      });

      it('should add only unique D to an array', () => {
        const dArray: ID[] = [{ id: 123 }, { id: 456 }, { id: 31759 }];
        const dCollection: ID[] = [{ id: 123 }];
        expectedResult = service.addDToCollectionIfMissing(dCollection, ...dArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const d: ID = { id: 123 };
        const d2: ID = { id: 456 };
        expectedResult = service.addDToCollectionIfMissing([], d, d2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(d);
        expect(expectedResult).toContain(d2);
      });

      it('should accept null and undefined values', () => {
        const d: ID = { id: 123 };
        expectedResult = service.addDToCollectionIfMissing([], null, d, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(d);
      });

      it('should return initial array if no D is added', () => {
        const dCollection: ID[] = [{ id: 123 }];
        expectedResult = service.addDToCollectionIfMissing(dCollection, undefined, null);
        expect(expectedResult).toEqual(dCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
