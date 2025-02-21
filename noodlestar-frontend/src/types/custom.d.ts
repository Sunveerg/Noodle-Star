declare module '*.png' {
  const value: string;
  export default value;
}

declare module '*.module.css' {
  const classes: { [key: string]: string };
  export default classes;
}

declare module 'papaparse' {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const Papa: any;
  export default Papa;
}
