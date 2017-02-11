prologues:=3;

verbatimtex
%&latex 
\documentclass[10pt]{article}
\usepackage{color}
\begin{document}
etex

beginfig(0);
path a,b,c,d,e;
draw(-5cm,4cm)--(-5cm,-1cm)--(0cm,-1cm);
label.lft(btex $p_A$ etex,(-5cm,3.8cm));

a=(-5cm,3.0cm)--(-4.0cm,-1cm);
b=(-5cm,2.0cm)--(-3.0cm,-1cm);
c=(-5cm,1.0cm)--(-1.0cm,-1cm);
d=(-5cm,3.0cm)--a intersectionpoint b--b intersectionpoint c--(-1.0cm,-1cm);

e=(-5cm,-0.2cm)--(0cm,-0.2cm);
draw a;
draw a intersectionpoint b--(-3.0cm,-1cm);
draw b intersectionpoint c--(-1.0cm,-1cm);
pickup pencircle scaled 1pt;
draw d;
label.top(btex $D_A$ etex,(-1.5cm,-0.7cm));
pickup defaultpen;
z1=d intersectionpoint e;
draw (-5cm,ypart(z1))-- z1 -- (xpart(z1),-1cm) dashed evenly;
label.lft(btex $p^*_A$ etex,(-5cm,ypart(z1)));
label.bot(btex $q_A$ etex,(-0.2cm,-1cm));

pickup pencircle scaled 1pt;
draw (xpart(z1),-1cm)--(xpart(z1),3cm);
pickup defaultpen;
label.top(btex $S_A$ etex,(xpart(z1),3cm));


z2=a intersectionpoint e;
draw z2--(xpart(z2),-1cm) dashed evenly;

z3=b intersectionpoint e;
draw z3--(xpart(z3),-1cm) dashed evenly;

label(btex $\underbrace{\hskip12mm}$ etex scaled 0.6,(-4.65cm,-1.15cm));
label(btex $\underbrace{\hskip10mm}$ etex scaled 0.6,(-3.9cm,-1.15cm));
label(btex $\underbrace{\hskip15mm}$ etex scaled 0.6,(-3.05cm,-1.15cm));

label(btex $q^*_{1A}$ etex scaled 0.8,(-4.65cm,-1.4cm));
label(btex $q^*_{2A}$ etex scaled 0.8,(-3.9cm,-1.4cm));
label(btex $q^*_{3A}$ etex scaled 0.8,(-3.05cm,-1.4cm));
endfig;




	   end

