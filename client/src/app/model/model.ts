export interface Coordinates {
    lat: number;
    lng: number;
}

export interface Fed {
    time: string | null;
    username: string | null;
}

export interface Cat {
    catId: string;
    profilePhoto: string;
    name: string;
    coordinates: Coordinates;
    fed: Fed;
}

export interface CatList {
    searchCoordinates: Coordinates;
    catList: Cat[];
}

export interface Update {
    location: string;
    time: string;
    username: string;
  }
  
  export interface CatInfo {
    catId: string;
    name: string;
    age: string;
    sterilization: boolean;
    personalityTraits: string[];
    dietLikes: string[];
    dietDislikes: string[];
    feedingNotes: string[];
    frequentLocations: Coordinates[];
    fedUpdates: Update[];
    seenUpdates: Update[];
  }
