#!/usr/bin/env python3
"""Assemble index.html (une seule page autonome) à partir de src/app.html + polices + librairies."""
import base64, os, re
R = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
V = os.path.join(R, 'tools', 'vendor')
s = open(os.path.join(R, 'src', 'app.html'), encoding='utf-8').read()
fonts = [('Bricolage Grotesque', w, 'bricolage-grotesque-latin-%d-normal.woff2' % w) for w in (600, 700, 800)] + \
        [('Figtree', w, 'figtree-latin-%d-normal.woff2' % w) for w in (400, 500, 600, 700)]
css = ''.join('@font-face{font-family:"%s";font-weight:%d;font-style:normal;font-display:swap;src:url(data:font/woff2;base64,%s) format("woff2")}\n'
              % (f, w, base64.b64encode(open(os.path.join(V, 'fonts', n), 'rb').read()).decode()) for f, w, n in fonts)
s = s.replace('/*@@FONTS@@*/', css)
s = s.replace('/*@@JSBARCODE@@*/', open(os.path.join(V, 'JsBarcode.all.min.js'), encoding='utf-8').read().replace('</script', '<\\/script'))
qr = open(os.path.join(V, 'qrcode.js'), encoding='utf-8').read()
qr = qr[:qr.index('(function (factory)')]  # on garde la fonction qrcode() sans l'emballage module
qr += '''
function qrSvg(text){
  var q=qrcode(0,'M');q.addData(text);q.make();
  var n=q.getModuleCount(),d='';
  for(var r=0;r<n;r++)for(var c=0;c<n;c++)if(q.isDark(r,c))d+='M'+c+' '+r+'h1v1h-1z';
  return '<svg viewBox="-2 -2 '+(n+4)+' '+(n+4)+'" xmlns="http://www.w3.org/2000/svg" shape-rendering="crispEdges" role="img" aria-label="QR code du lien de téléchargement"><path d="'+d+'" fill="#1D1640"/></svg>';
}
'''
s = s.replace('/*@@QR@@*/', qr)
s = s.replace('/*@@LOGO@@*/', 'data:image/png;base64,' + base64.b64encode(open(os.path.join(V, 'logo.png'), 'rb').read()).decode())
assert '@@' not in s, re.findall(r'/\*@@\w+@@\*/', s)
open(os.path.join(R, 'index.html'), 'w', encoding='utf-8').write(s)
print('index.html', len(s) // 1024, 'Ko')
