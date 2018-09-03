import { IUser } from 'app/core/user/user.model';

export interface IFamily {
    id?: number;
    name?: string;
    members?: IUser[];
}

export class Family implements IFamily {
    constructor(public id?: number, public name?: string, public members?: IUser[]) {}
}
