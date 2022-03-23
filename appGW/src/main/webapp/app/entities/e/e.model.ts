import { IC } from 'app/entities/c/c.model';

export interface IE {
  id?: number;
  uid?: string | null;
  entCCCuid?: number | null;
  entEEEcod?: string | null;
  entEEEdsc?: string | null;
  entCCCuid?: IC | null;
}

export class E implements IE {
  constructor(
    public id?: number,
    public uid?: string | null,
    public entCCCuid?: number | null,
    public entEEEcod?: string | null,
    public entEEEdsc?: string | null,
    public entCCCuid?: IC | null
  ) {}
}

export function getEIdentifier(e: IE): number | undefined {
  return e.id;
}
