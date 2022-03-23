import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IC, C } from '../c.model';

import { CService } from './c.service';

describe('C Service', () => {
  let service: CService;
  let httpMock: HttpTestingController;
  let elemDefault: IC;
  let expectedResult: IC | IC[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      uid: 'AAAAAAA',
      entCCCcod: 'AAAAAAA',
      entCCCdsc: 'AAAAAAA',
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

    it('should create a C', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new C()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a C', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          entCCCcod: 'BBBBBB',
          entCCCdsc: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a C', () => {
      const patchObject = Object.assign(
        {
          uid: 'BBBBBB',
          entCCCcod: 'BBBBBB',
          entCCCdsc: 'BBBBBB',
        },
        new C()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of C', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          entCCCcod: 'BBBBBB',
          entCCCdsc: 'BBBBBB',
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

    it('should delete a C', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCToCollectionIfMissing', () => {
      it('should add a C to an empty array', () => {
        const c: IC = { id: 123 };
        expectedResult = service.addCToCollectionIfMissing([], c);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(c);
      });

      it('should not add a C to an array that contains it', () => {
        const c: IC = { id: 123 };
        const cCollection: IC[] = [
          {
            ...c,
          },
          { id: 456 },
        ];
        expectedResult = service.addCToCollectionIfMissing(cCollection, c);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a C to an array that doesn't contain it", () => {
        const c: IC = { id: 123 };
        const cCollection: IC[] = [{ id: 456 }];
        expectedResult = service.addCToCollectionIfMissing(cCollection, c);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(c);
      });

      it('should add only unique C to an array', () => {
        const cArray: IC[] = [{ id: 123 }, { id: 456 }, { id: 83124 }];
        const cCollection: IC[] = [{ id: 123 }];
        expectedResult = service.addCToCollectionIfMissing(cCollection, ...cArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const c: IC = { id: 123 };
        const c2: IC = { id: 456 };
        expectedResult = service.addCToCollectionIfMissing([], c, c2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(c);
        expect(expectedResult).toContain(c2);
      });

      it('should accept null and undefined values', () => {
        const c: IC = { id: 123 };
        expectedResult = service.addCToCollectionIfMissing([], null, c, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(c);
      });

      it('should return initial array if no C is added', () => {
        const cCollection: IC[] = [{ id: 123 }];
        expectedResult = service.addCToCollectionIfMissing(cCollection, undefined, null);
        expect(expectedResult).toEqual(cCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
