export interface Cat {
    name: string;
    feedingnotes: string;
    frequentLocations: Location[];
    imageUrl: string;
  }
  
export interface Location {
lat: number;
long: number;
}
  
export interface CatList {
cats: Cat[];
}
  