export interface IGift {
    id?: number;
    name?: string;
    description?: any;
    url?: string;
    giftListId?: number;
}

export class Gift implements IGift {
    constructor(public id?: number, public name?: string, public description?: any, public url?: string, public giftListId?: number) {}
}
