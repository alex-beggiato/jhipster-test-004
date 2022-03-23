import { IA } from 'app/entities/a/a.model';

export interface IB {
  id?: number;
  uid?: string | null;
  entAAAuid?: number | null;
  entBBBcod?: string | null;
  entBBBdsc?: string | null;
  entAAAuid?: IA | null;
}

export class B implements IB {
  constructor(
    public id?: number,
    public uid?: string | null,
    public entAAAuid?: number | null,
    public entBBBcod?: string | null,
    public entBBBdsc?: string | null,
    public entAAAuid?: IA | null
  ) {}
}

export function getBIdentifier(b: IB): number | undefined {
  return b.id;
}
