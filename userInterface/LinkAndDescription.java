// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import java.net.URL;

class LinkAndDescription
{
    private String title;
    private URL url;
    
    public LinkAndDescription(final URL url, final String title) {
        this.title = title;
        this.url = url;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public URL getURL() {
        return this.url;
    }
    
    public boolean equals(final Object o) {
        return this.url != null && this.url.equals(o);
    }
    
    public String toString() {
        return this.title;
    }
}
