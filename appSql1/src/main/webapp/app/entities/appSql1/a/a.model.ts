import { IB } from 'app/entities/appSql1/b/b.model';

export interface IA {
  id?: number;
  uid?: string | null;
  entAAAcod?: string | null;
  entAAAdsc?: string | null;
  uids?: IB[] | null;
}

export class A implements IA {
  constructor(
    public id?: number,
    public uid?: string | null,
    public entAAAcod?: string | null,
    public entAAAdsc?: string | null,
    public uids?: IB[] | null
  ) {}
}

export function getAIdentifier(a: IA): number | undefined {
  return a.id;
}
