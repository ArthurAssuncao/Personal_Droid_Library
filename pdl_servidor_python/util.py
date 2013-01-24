#coding: utf-8

import re
from Constantes import *

def remove_tags(texto):
    return TAG_RE.sub('', texto)
