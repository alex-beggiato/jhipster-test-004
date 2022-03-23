export interface ID {
  id?: number;
  uid?: string | null;
  entDDDcod?: string | null;
  entDDDdsc?: string | null;
}

export class D implements ID {
  constructor(public id?: number, public uid?: string | null, public entDDDcod?: string | null, public entDDDdsc?: string | null) {}
}

export function getDIdentifier(d: ID): number | undefined {
  return d.id;
}
