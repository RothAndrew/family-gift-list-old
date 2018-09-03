import { IUser } from 'app/core/user/user.model';

export interface IFamily {
    id?: number;
    name?: string;
    members?: IUser[];
    owners?: IUser[];
    admins?: IUser[];
}

export class Family implements IFamily {
    constructor(public id?: number, public name?: string, public members?: IUser[], public owners?: IUser[], public admins?: IUser[]) {}
}
