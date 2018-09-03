import { IGift } from 'app/shared/model//gift.model';

export interface IGiftList {
    id?: number;
    gifts?: IGift[];
    userLogin?: string;
    userId?: number;
}

export class GiftList implements IGiftList {
    constructor(public id?: number, public gifts?: IGift[], public userLogin?: string, public userId?: number) {}
}
