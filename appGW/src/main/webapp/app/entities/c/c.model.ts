import { IE } from 'app/entities/e/e.model';

export interface IC {
  id?: number;
  uid?: string | null;
  entCCCcod?: string | null;
  entCCCdsc?: string | null;
  uids?: IE[] | null;
}

export class C implements IC {
  constructor(
    public id?: number,
    public uid?: string | null,
    public entCCCcod?: string | null,
    public entCCCdsc?: string | null,
    public uids?: IE[] | null
  ) {}
}

export function getCIdentifier(c: IC): number | undefined {
  return c.id;
}
