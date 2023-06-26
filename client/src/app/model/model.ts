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
    comments: string;
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

  export interface UpdateForm {
    type: string;
    catId: string;
    foodType: string; // for food type checkboxes
    waterStatus: string; // for water bowl status radio buttons
    location: string;
    datetime: Date;
    comments: string;
    photo: File;
}

export interface AddCatForm {
    profilePhoto: File;
    locationAddress: string;
    username: string;
    name: string;
    gender: string;
    birthday: Date;
    sterilization: boolean;
    personalityTraits: string[];
    dietLikes: string[];
    dietDislikes: string[];
    feedingNotes: string[];
}

export interface AddFundraiserForm {
    photo: File;
    catId: string;
    username: string;
    title: string;
    description: string;
    donationGoal: number;
    deadline: Date;
}


export interface CatRequest {
    catId: string;
    username: string;
    approved: string;
    name: string; //cat name
}

export interface FundraiserRequest {
    catId: string;
    fundId: string;
    username: string;
    approved: string;
    title: string; //fundraiser title
}

export interface CatRequests {
    pendingCats: CatRequest[];
}

export interface FundraiserRequests {
    pendingFunds: FundraiserRequest[];
}


