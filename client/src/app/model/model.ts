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
    photoUrl?: string;
  }

  export interface FundraiserUpdate {
    title: string;
    timeLeft: string;
  }
  
  export interface CatInfo {
    catId: string;
    photoUrls: string[];
    name: string;
    gender: string;
    age: string;
    sterilization: boolean;
    personalityTraits: string[];
    dietLikes: string[];
    dietDislikes: string[];
    feedingNotes: string[];
    frequentLocations: Coordinates[];
    fedUpdates: Update[];
    seenUpdates: Update[];
    fundraiserUpdates: FundraiserUpdate;
  }

  export interface Donation {
    username: string;
    amount: number;
    donationDate: string;
  }
  
  export interface Fundraiser {
    fundId: string;
    catId: string;
    username: string;
    photoUrl: string;
    title: string;
    description: string;
    donationGoal: number;
    deadline: string;
    timeRemaining: string;
    donations: Donation[];
  }
