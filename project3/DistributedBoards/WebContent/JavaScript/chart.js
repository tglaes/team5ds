var graphdata1 = {
    linecolor: "blue",
    title: "Boards",
    values: [
        { X: "Monday", Y: 10.00 },
        { X: "Tuesday", Y: 20.00 },
        { X: "Wednesday", Y: 40.00 },
        { X: "Thursday", Y: 40.00 },
        { X: "Friday", Y: 40.00 },
        { X: "Saturday", Y: 40.00 },
        { X: "Sunday", Y: 34.00 }
    ]
};
var graphdata2 = {
    linecolor: "lightgreen",
    title: "Posts",
    values: [
        { X: "Monday", Y: 20.00 },
        { X: "Tuesday", Y: 30.00 },
        { X: "Wednesday", Y: 50.00 },
        { X: "Thursday", Y: 50.00 },
        { X: "Friday", Y: 50.00 },
        { X: "Saturday", Y: 50.00 },
        { X: "Sunday", Y: 44.00 }
    ]
};
var graphdata3 = {
    linecolor: "red",
    title: "Comments",
    values: [
        { X: "Monday", Y: 30.00 },
        { X: "Tuesday", Y: 40.00 },
        { X: "Wednesday", Y: 60.00 },
        { X: "Thursday", Y: 60.00 },
        { X: "Friday", Y: 60.00 },
        { X: "Saturday", Y: 60.00 },
        { X: "Sunday", Y: 54.00 }
    ]
};
var graphdata4 = {
    linecolor: "orange",
    title: "Likes",
    values: [
        { X: "Monday", Y: 40.00 },
        { X: "Tuesday", Y: 50.00 },
        { X: "Wednesday", Y: 70.00 },
        { X: "Thursday", Y: 70.00 },
        { X: "Friday", Y: 70.00 },
        { X: "Saturday", Y: 70.00 },
        { X: "Sunday", Y: 64.00 }
    ]
};
var Piedata = {
    linecolor: "Random",
    title: "Profit",
    values: [
        { X: "Boards", Y: 50},   /*"###NumberBoards###"*/
        { X: "Posts", Y:20},	 /*"###NumberPosts###"*/
        { X: "Comments", Y: 30}, /*"###NumberComments###"*/
        { X: "Likes", Y: 60.00 }
    ]
};

$(function () {

    $("#Linegraph").SimpleChart({
        ChartType: "Line",
        toolwidth: "50",
        toolheight: "25",
        axiscolor: "#E6E6E6",
        textcolor: "#6E6E6E",
        showlegends: true,
        data: [graphdata4, graphdata3, graphdata2, graphdata1],
        legendsize: "120",
        legendposition: 'bottom',
        xaxislabel: '',
        title: '',
        yaxislabel: '# Day'
    });
    $("#Piegraph").SimpleChart({
        ChartType: "Pie",
        toolwidth: "50",
        toolheight: "25",
        axiscolor: "#E6E6E6",
        textcolor: "#6E6E6E",
        showlegends: false,
        showpielables: true,
        data: [Piedata],
        legendsize: "250",
        legendposition: 'right',
        xaxislabel: 'Days',
        title: '',
        yaxislabel: 'Profit in $'
    });
});
