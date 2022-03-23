import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IB, B } from '../b.model';

import { BService } from './b.service';

describe('B Service', () => {
  let service: BService;
  let httpMock: HttpTestingController;
  let elemDefault: IB;
  let expectedResult: IB | IB[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      uid: 'AAAAAAA',
      entAAAuid: 0,
      entBBBcod: 'AAAAAAA',
      entBBBdsc: 'AAAAAAA',
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

    it('should create a B', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new B()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a B', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          entAAAuid: 1,
          entBBBcod: 'BBBBBB',
          entBBBdsc: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a B', () => {
      const patchObject = Object.assign(
        {
          uid: 'BBBBBB',
          entAAAuid: 1,
        },
        new B()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of B', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          entAAAuid: 1,
          entBBBcod: 'BBBBBB',
          entBBBdsc: 'BBBBBB',
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

    it('should delete a B', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBToCollectionIfMissing', () => {
      it('should add a B to an empty array', () => {
        const b: IB = { id: 123 };
        expectedResult = service.addBToCollectionIfMissing([], b);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(b);
      });

      it('should not add a B to an array that contains it', () => {
        const b: IB = { id: 123 };
        const bCollection: IB[] = [
          {
            ...b,
          },
          { id: 456 },
        ];
        expectedResult = service.addBToCollectionIfMissing(bCollection, b);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a B to an array that doesn't contain it", () => {
        const b: IB = { id: 123 };
        const bCollection: IB[] = [{ id: 456 }];
        expectedResult = service.addBToCollectionIfMissing(bCollection, b);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(b);
      });

      it('should add only unique B to an array', () => {
        const bArray: IB[] = [{ id: 123 }, { id: 456 }, { id: 23941 }];
        const bCollection: IB[] = [{ id: 123 }];
        expectedResult = service.addBToCollectionIfMissing(bCollection, ...bArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const b: IB = { id: 123 };
        const b2: IB = { id: 456 };
        expectedResult = service.addBToCollectionIfMissing([], b, b2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(b);
        expect(expectedResult).toContain(b2);
      });

      it('should accept null and undefined values', () => {
        const b: IB = { id: 123 };
        expectedResult = service.addBToCollectionIfMissing([], null, b, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(b);
      });

      it('should return initial array if no B is added', () => {
        const bCollection: IB[] = [{ id: 123 }];
        expectedResult = service.addBToCollectionIfMissing(bCollection, undefined, null);
        expect(expectedResult).toEqual(bCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
