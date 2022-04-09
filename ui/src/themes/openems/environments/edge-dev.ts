import { Environment } from "src/environments";

export const environment: Environment = {
    theme: "OpenEMS",

    uiTitle: "fortiss Energy Lab",
    edgeShortName: "OpenEMS",
    edgeLongName: "Open Energy Management System",

    backend: 'OpenEMS Edge',
    url: "ws://" + location.hostname + ":8085",

    production: false,
    debugMode: true,
};
